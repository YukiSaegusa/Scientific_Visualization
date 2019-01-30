folder_add=`pwd`/results/`date +%Y%m%d-%H-%M-%S`

mkdir results
mkdir $folder_add

cp cascade.lammps $folder_add
cp Cu.lat $folder_add

echo "#! /bin/bash" >> $folder_add/calc_cascade.sh
echo "#PBS -q commonfp" >> $folder_add/calc_cascade.sh
echo "#PBS -l select=1:ncpus=8:mem=5gb">> $folder_add/calc_cascade.sh
echo "#PBS -j oe">> $folder_add/calc_cascade.sh
echo "#PBS -l place=pack:shared">> $folder_add/calc_cascade.sh
echo "source /etc/profile.d/modules.sh">> $folder_add/calc_cascade.sh
echo "module load lmp/10Feb15">> $folder_add/calc_cascade.sh
echo "cd ${folder_add}">> $folder_add/calc_cascade.sh
echo 'mpirun -np 8 /ap/lammps-10Feb15/lmp_mpiicpc_O2_fpmodel_precise <cascade.lammps> ./log -var rand $RANDOM' >> $folder_add/calc_cascade.sh

cd ${folder_add}
qsub calc_cascade.sh